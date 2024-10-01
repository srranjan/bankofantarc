Begin Phase 3 - Enter Kustomize
The old code adapted for Kustomize embellishments:
I am having some issues in using mysql through helm, hence using mysql stuff in k8 folder, the old stuff.
kubectl apply -f mysql-pv.yaml -n corecrud
Actually, above the -n is redundant, most probably.
kubectl create namespace corecrud
kubectl apply -f mysql-pvc.yaml -n corecrud
kubectl apply -f mysql_k8.yaml -n corecrud
The above from k8 folder, whereas the below from base folder.
Validation of kustomization:
kubectl apply --dry-run=client -o yaml -k ./
To actuall deploy stuff:
kubectl apply  -k ./
Once the above was successful in installing all the stuff in the kustomization.yaml in base folder, the service will be available at the host address of minikube ip and the port that could be found with kubectl get svc reactsvc -n corecrud, the port to the right of 9095 after:
minikube ip
192.168.49.2
kubectl get svc reactsvc -n corecrud
NAME       TYPE       CLUSTER-IP      EXTERNAL-IP   PORT(S)          AGE
reactsvc   NodePort   10.110.72.224   <none>        9095:30005/TCP   4m45s

This gives the required curl:
curl http://192.168.49.2:30005/backsvc/clients
But a simpler way to arrive at the above url is this:
minikube service reactsvc -n corecrud --url

To install mysql client in ubuntu:
sudo apt update && sudo apt install mysql-client -y

One extra step needed for this react service with mysql is to create the required table, the code in this case does 
not do it automatically. This is one of the new vagaries of the new mysql driver, it looks.
minikube service mysql -n corecrud --url
http://192.168.49.2:32066

Now from mysql folder:
mysql -h 192.168.49.2 -P 32066 -u <username> -p
use corecruddb;
source createschema.sql;


Now finally, finally the curl looks to be successful:
curl http://192.168.49.2:30005/backsvc/clients
[]
So till kustomization, things have worked.

Next, I am trying to validate the kustomize by going to dev folder and try to set up things in corecrud-dev namespace.
Looks it will not work unless mysql is also in the same namespace (based on error, the reactsvc fails due to not resolving 
mysql host, and this is contrary to what Gemini had told me earlier, it looks to me).
I deleted the mysql from corecrud namespace and then recreated in the corecrud-dev:
kubectl apply -f mysql-pv.yaml  # I had to delete pv also and then recreate it even though it is independent of ns. I found the hard way
kubectl apply -f mysql-pvc.yaml -n corecrud-dev
kubectl apply -f mysql_k8.yaml -n corecrud-dev

And finally I needed to again do the steps leading to source createschema.sql above.

And then the final validation step 
curl http://192.168.49.2:30005/backsvc/clients
[]

Thus the change of namespace with customization works for corecrud-dev. I assume it will also work for stage.

Sometimes, you may need extra resource, but not sure in this case:
minikube start --memory=4096 --cpus=4


End Phase 3 - Enter Kustomize





