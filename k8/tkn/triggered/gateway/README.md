This folder has just been copied wholesale from kustomize/istio, and with the purpose of more streamline
work on triggered tekton flow.
The original file will be adapted not for any application but for tekton listener.

https://istio.io/latest/docs/tasks/traffic-management/ingress/ingress-control/
kubectl get crd gateways.gateway.networking.k8s.io &> /dev/null || \
  { kubectl apply -f https://github.com/kubernetes-sigs/gateway-api/releases/download/v1.1.0/standard-install.yaml; }
PostScript: Run this before setting gateway:
kubectl wait --for=condition=programmed gtw common-gateway
cd ~/devops3/bankofantarc/k8/tkn/triggered/gateway
kubectl apply -f mygtw.yaml 
Note: Probably, to be on the safe side, it might be better gtw and route are deployed after demo profile is deployed, as shown below.
kubectl wait --for=condition=programmed gtw common-gateway 
I have a problem here, the gateway is not coming up, nor is it giving any error on kubectl apply command. May be the problem is resolved after the following command(or may be just use kubectl get gtw common-gateway):
istioctl install --set profile=demo --set values.global.hub=docker.io/istio

kubectl apply -f myroute.yaml  
{
If you need to delete stuff:
kubectl delete httproute myrouter
kubectl delete  gtw common-gateway
}
kubectl get HTTPRoute myrouter
NAME       HOSTNAMES              AGE
myrouter   ["bankofantarc.com"]   36s



minikube tunnel

Now, I can get this beauty:
kubectl get gtw common-gateway  
NAME             CLASS   ADDRESS         PROGRAMMED   AGE
common-gateway   istio   10.96.125.226   True         4m37s
. ./geturl.sh common-gateway default
INGRESS_HOST 10.96.125.226
INGRESS_PORT 80
PostScript: Of course, the host changes from 10.96.125.226 to something else 10.105.19.243 on bare metal.
My next was of course to have this artificial entry in /etc/hosts:
10.96.125.226   bankofantarc.com

Now I try to extend my success with triggered with this gateway based triggered, using this one:
ngrok http http://bankofantarc.com:80

But this phase is unsuccessful, as in the ngrok window, I see git push webhook event coming:
11:42:33.518 EST POST /                         404 Not Found    
but, of course, http://bankofantarc.com:80 is not working, which I was hoping to work with minikube tunnel.
I will not solve this issue for now, may be sometimes later.
The conclusion is this: (1) The istio gateway functionality had worked for /clients application URL (requiring also mysql) and (2) triggered tekton pipeline from webhook github push even also worked, but (3) I could not marry 
(1) and (2) for gateway based gibhub push webhook event triggering for complete tekton build all the way to image pushed to docker hub. 
To sum up, the success of (2) is captured  in /k8/kustomize/istio/README.md.
Success of (1) is captured in ./k8/tkn/triggered/README.md.
And, of course, (3), i.e., I am at ./k8/tkn/triggered/gateway/README.md
