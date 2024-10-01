This folder has just been copied wholesale from kustomize/istio, and with the purpose of more streamline
work on triggered tekton flow.
The original file will be adapted not for any application but for tekton listener.

https://istio.io/latest/docs/tasks/traffic-management/ingress/ingress-control/
kubectl get crd gateways.gateway.networking.k8s.io &> /dev/null || \
  { kubectl apply -f https://github.com/kubernetes-sigs/gateway-api/releases/download/v1.1.0/standard-install.yaml; }

cd ~/devops3/bankofantarc/k8/tkn/triggered/gateway
kubectl apply -f mygtw.yaml 
Note: Probably, to be on the safe side, it might be better gtw and route are deployed after demo profile is deployed, as shown below.
kubectl wait --for=condition=programmed gtw common-gateway 
I have a problem here, the gateway is not coming up, nor is it giving any error on kubectl apply command. May be the problem is resolved after the following command:
istioctl install --set profile=demo --set values.global.hub=docker.io/istio

kubectl apply -f myroute.yaml  
{
If you need to delete stuff:
kubectl delete httproute myrouter
kubectl delete  gtw common-gateway
}

minikube tunnel

Now, I can get this beauty:
kubectl get gtw common-gateway  
NAME             CLASS   ADDRESS         PROGRAMMED   AGE
common-gateway   istio   10.96.125.226   True         4m37s
. ./geturl.sh common-gateway default
INGRESS_HOST 10.96.125.226
INGRESS_PORT 80

My next was of course to have this artificial entry in /etc/hosts:
10.96.125.226   bankofantarc.com
And then I got the following beauty which seems to suggest the Istio gateway and route configured work.
curl "http://bankofantarc.com/backsvc/clients"
[]

