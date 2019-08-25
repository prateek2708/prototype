#Pushing image to repository
##tag image
docker tag sample-nodejs-app:latest gcr.io/first-firestore/node-app:latest

##configure authentication
gcloud auth configure-docker
https://cloud.google.com/container-registry/docs/advanced-authentication

##push image to repository
docker push gcr.io/first-firestore/node-app:latest

##installing kubectl
sudo apt-get update && sudo apt-get install -y apt-transport-https
curl -s https://packages.cloud.google.com/apt/doc/apt-key.gpg | sudo apt-key add -
echo "deb https://apt.kubernetes.io/ kubernetes-xenial main" | sudo tee -a /etc/apt/sources.list.d/kubernetes.list
sudo apt-get update
sudo apt-get install -y kubectl

##Create kubernetes cluster
gcloud config set project [PROJECT_ID]
##assign zone to cluster
gcloud config set compute/zone us-west1-a
##create kubernetes cluster
gcloud container clusters create node-app
##get credentials for cluster
gcloud container clusters get-credentials node-app

## create deployment using file
kubectl create -f deployment.yaml
##describe deployment
kubectl describe deployments
##get deployment
kubectl get deployments
## delete deployment
kubectl delete -f deployment.yaml

##expose kubernetes service
kubectl expose -f service.yaml
## apply service which assigns public ip 
kubectl apply -f service.yaml 
##describe service
kubectl get svc kub-service
## delete service
kubectl delete service kub-service


##References
https://nodejs.org/de/docs/guides/nodejs-docker-webapp/
https://cloud.google.com/kubernetes-engine/docs/how-to/exposing-apps
https://kubernetes.io/docs/tutorials/stateless-application/expose-external-ip-address/
https://kubernetes.io/docs/concepts/services-networking/connect-applications-service/
https://medium.com/rackbrains/kubernetes-101-part-2-deploying-apps-to-kubernetes-cluster-fcad2615d59