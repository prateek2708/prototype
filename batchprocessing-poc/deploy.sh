#!/bin/bash -x

fbEnv="${1:-local}";
commitRange="$2";
here="`dirname $0`";
sha1="`git rev-parse HEAD | cut -c-7`";
is_ci=${CI:-false};

do_deploy=false;
if [ ! -z "$commitRange" ]; then
  echo "Commit range ${commitRange} provided, conditional evaluation follows...";
  # If the config changed, deploy
  if git diff $commitRange --name-only | grep -q "^config-${fbEnv}\.json$"; then
    echo "Changed config for ${fbEnv}";
    do_deploy=true;
  fi;
  # If the version was bumped, deploy
  if git diff $commitRange --name-only | grep -q "^\.version$"; then
    echo "Bumped version";
    do_deploy=true; 
  fi;
  # If the code changed, deploy
  if git diff $commitRange --name-only | grep -q "^functions\/"; then
    echo "Changed source code";
    do_deploy=true;
  fi;
else
  # if no commit range specified, assume deploy is needed
  do_deploy=true;
fi;

echo "Needs deploy?: ${do_deploy}";

if $do_deploy; then
  if $is_ci; then
    interactive="--non-interactive"
  else
    interactive="--interactive";
  fi
  "${here}/functions/node_modules/.bin/firebase" --project=${fbEnv} $interactive functions:config:unset configuration;
  "${here}/functions/node_modules/.bin/firebase" --project=${fbEnv} $interactive functions:config:set configuration="$( cat ${here}/config-${fbEnv}.json )";
  "${here}/functions/node_modules/.bin/firebase" --project=${fbEnv} $interactive functions:config:set configuration.version=$(cat ${here}/.version);
  "${here}/functions/node_modules/.bin/firebase" --project=${fbEnv} $interactive functions:config:set configuration.sha1=${sha1};
  "${here}/functions/node_modules/.bin/firebase" --project=${fbEnv} $interactive deploy --only functions:batchService,hosting,storage;
fi;
