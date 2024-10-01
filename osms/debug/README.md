Trying to capture any possible issues.

crc start --memory <number-in-mib> --cpus <number>
crc start --memory 21504 --cpus 6
The default value for the memory property is 10752 (=10.5 GB)

Begin Final Output after successful start
The server is accessible via web console at:
  https://console-openshift-console.apps-crc.testing

Log in as administrator:
  Username: kubeadmin
  Password: 4jLRB-bcbI2-QWELZ-FNchU

Log in as user:
  Username: developer
  Password: developer

Use the 'oc' command line interface:
  $ eval $(crc oc-env)
  $ oc login -u developer https://api.crc.testing:6443
End Final Output after successful start

Begin some output after successful developer login
oc new-project projantarc
Now using project "projantarc" on server "https://api.crc.testing:6443".

You can add applications to this project with the 'new-app' command. For example, try:

    oc new-app rails-postgresql-example

to build a new example application in Ruby. Or use kubectl to deploy a simple Kubernetes application:

    kubectl create deployment hello-node --image=registry.k8s.io/e2e-test-images/agnhost:2.43 -- /agnhost serve-hostname

End some output after successful developer login

eval $(crc oc-env)
oc login -u developer https://api.crc.testing:6443
oc new-project projantarc
Note: I created one projantarc before login, and that does not work.
In a different window, a different login, just in case:
eval $(crc oc-env)
oc login -u kubeadmin https://api.crc.testing:6443
cd /home/rranjan/devops5/bankofantarc/osms
oc apply -f  mystorage.yaml #using kubeadmin rather than developer
Reverting to developer probably for the 3 below:
oc apply -f    mysql-pv.yaml
oc apply -f    mysql-pvc.yaml
oc apply -f mysql_k8.yaml

I have to see whether these work:
kubectl get pods -l app=mysql where mysql is the label for the app
oc get pods -l app=mysql 


kubectl port-forward <mysql-pod-name> 3306:3306
oc port-forward <mysql-pod-name> 3306:3306
kubectl port-forward  mysql-8cf6f6cc4-7qpx5 3306:3306
oc port-forward  mysql-8cf6f6cc4-7qpx5 3306:3306
And, thanks God, after good deal of trial and error, I was able to connect to the pod mysql db like this:
mysql -h 127.0.0.1 -u root  -D corecruddb -P 3306 -p
Also, it looks the following worked on mysql client:
source createschema.sql; Of course, from the mysql dir.
It looks it worked.


Begin altentaive mysql playbook
 An alternative to above:
helm install mysql bitnami/mysql --values values.yaml

helm repo add bitnami https://charts.bitnami.com/bitnami
helm repo update
values.yaml:
auth:
  rootPassword: mypassword
  database: mydb

helm install mysql bitnami/mysql --values values.yaml

A note when using this method for mysql - rather than deploy, probably you have something called statefulset.
oc get all | grep mysql
Warning: apps.openshift.io/v1 DeploymentConfig is deprecated in v4.14+, unavailable in v4.10000+
pod/mysql-0   0/1     Running   0          15s
service/mysql            ClusterIP   10.217.5.199   <none>        3306/TCP   15s
service/mysql-headless   ClusterIP   None           <none>        3306/TCP   15s
statefulset.apps/mysql   0/1     15s


kubectl get pods -l app.kubernetes.io/name=mysql
kubectl exec -it <pod-name> -- mysql -u root -p mydb
kubectl exec -it mysql-0 -- mysql -u root -p corecruddb
It worked and I created the table on the mysql command line:
DROP TABLE IF EXISTS my_client;
CREATE TABLE my_client(
        id SERIAL PRIMARY KEY,
        name VARCHAR(255),
        phone VARCHAR(255),
        address VARCHAR(255),
        email VARCHAR(255),
        balance VARCHAR(255)
);
The advantage of this alternative method is that you don't need to use mysql cli at command line, rather reuse the stuff inside crc/k8s pod!!
So, largely this alternative method is also validated largely.

End altentaive mysql playbook

{



}

{
A possible cleanup cycle:
crc stop
crc delete 
crc cleanup
crc setup
crc start


}
{
Some debugging to see the Route related stuff is installed:
oc get clusteroperators | grep –i route
oc get clusteroperators 
oc get pods -n openshift-ingress

oc logs <podName> -n openshift-ingress
}

In a different window, a different login, just in case(because dry run failed as developer but is successful as kubeadmin, particularly probably due to the new kind version for Route:
eval $(crc oc-env)
oc login -u kubeadmin https://api.crc.testing:6443
kubectl apply --dry-run=client -o yaml -k ./

Everything looks to work but this test is not successful:
curl http://reactsvc-route-projantarc.apps-crc.testing:9095/backsvc/clients

I even peeked in the log, and there also things look honky dory:

ubectl logs reactsvc-8669b9b9fd-r7l5n

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.1.5)

2024-09-20T19:28:35.301Z  INFO 1 --- [           main] com.mypoc.rest.MyApp                     : Starting MyApp v0.0.1-SNAPSHOT using Java 17.0.12 with PID 1 (/app.jar started by 1000650000 in /)
2024-09-20T19:28:35.309Z  INFO 1 --- [           main] com.mypoc.rest.MyApp                     : No active profile set, falling back to 1 default profile: "default"
2024-09-20T19:28:39.853Z  INFO 1 --- [           main] .s.d.r.c.RepositoryConfigurationDelegate : Bootstrapping Spring Data R2DBC repositories in DEFAULT mode.
2024-09-20T19:28:40.440Z  INFO 1 --- [           main] .s.d.r.c.RepositoryConfigurationDelegate : Finished Spring Data repository scanning in 560 ms. Found 1 R2DBC repository interfaces.
2024-09-20T19:28:45.306Z  INFO 1 --- [           main] o.s.b.a.e.web.EndpointLinksResolver      : Exposing 3 endpoint(s) beneath base path '/actuator'
2024-09-20T19:28:46.926Z  INFO 1 --- [           main] o.s.b.web.embedded.netty.NettyWebServer  : Netty started on port 9095
2024-09-20T19:28:46.955Z  INFO 1 --- [           main] com.mypoc.rest.MyApp                     : Started MyApp in 14.315 seconds (process running for 17.027)



Begin Playbook to run docker-compose 
I tried to use docker-compose for service and mysql, but faced problem. Merely installing docker and dokcer-compose did not work. I was finally able to resolve by 
following this URL:
https://help.hcl-software.com/bigfix/10.0/mcm/MCM/Install/install_docker_ce_docker_compose_on_rhel_8.html

In effect, I had to run the following sequence of commands, even though I had already installed docker and docker-compose:


sudo dnf config-manager --add-repo=https://download.docker.com/linux/centos/docker-ce.repo
sudo dnf install --nobest docker-ce-rootless-extras-24.0.
sudo dnf install https://download.docker.com/linux/centos/7/x86_64/stable/Packages/containerd.io-1.2.6-3.3.el7.x86_64.rp
sudo dnf install docker-ce --allowerasing
sudo systemctl enable --now docker

And finally the following worked:
sudo docker-compose -f docker-compose-svc.yml up
Somehow, I am not able to push env variable in the svc using the above.

mysql -h server1.p2p.net -P 3306 -u root -p

{
https://www.redhat.com/sysadmin/build-deploy-application-openshift
Some commands:
oc project <name>
oc delete project <name>
oc get project
HELLO_URL=$(oc get route hello-world -o jsonpath='{.spec.host}')

oc config view
oc config current-context
}

End Playbook to run docker-compose 

