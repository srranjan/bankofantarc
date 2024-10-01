Begin Phase 5 ARGOCD

minikube start --memory=4096 --cpus=4

kubectl create ns argocd

kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml

cd ~/devops3/bankofantarc/k8/
kubectl create namespace corecrud
kubectl apply -f mysql-pv.yaml -n corecrud # ns corecrud not required here really
kubectl apply -f mysql-pvc.yaml -n corecrud
kubectl apply -f mysql_k8.yaml -n corecrud

I also need mysql client here, a side step really.
sudo apt update && sudo apt install mysql-client -y
minikube service mysql -n corecrud --url
http://192.168.49.2:31566
cd ../mysql
mysql -h 192.168.49.2 -P 31566 -u root -p
use corecruddb;
source createschema.sql;

Note: Some of the side steps are the same as mentioned in kustomize folder READEM.md.

Now comes the moment of truth. 

cd ../k8/tkn/argoc

kubectl apply -f  myargocdapp.yaml

Oh, I forgot, need to have argocd cli too!

curl -sSL -o argocd-linux-amd64 https://github.com/argoproj/argo-cd/releases/latest/download/argocd-linux-amd64

Now, returning to the main stuff:
argocd app list
FATA[0000] Argo CD server address unspecified 

kubectl port-forward svc/argocd-server -n argocd 8080:443

kubectl get secret argocd-initial-admin-secret -n argocd -o jsonpath='{.data.password}' | base64 --decode
admin/cL53sXOMEAgZVHWw

argocd login localhost:8080
logged in using admin/cL53sXOMEAgZVHWw

argocd app list
argocd app sync myargocdapp

minikube service reactsvc -n corecrud --url
http://192.168.49.2:30005

curl http://192.168.49.2:30005/backsvc/clients
[]

It looks argocd app worked, was able to get kustomization.yaml from base folder and duly download the reactsvc image from docker hub and deploy the deployment and service
All the 5 phases have been done on minikube till now.

End Phase 5 ARGOCD
