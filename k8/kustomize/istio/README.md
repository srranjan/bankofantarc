Begin Phase 3a
The following successful work of Istio gateway configuration was done after Phase 4 and 5 (most probably Tekton and argoCD), but can be considered a value addition to Phase 3 (kustomization for k8s). Let me call this work Phase 3a, so that later I might call some of the incremenal works as Phase 4a, 5a etc.


https://istio.io/latest/docs/tasks/traffic-management/ingress/ingress-control/
kubectl get crd gateways.gateway.networking.k8s.io &> /dev/null || \
  { kubectl apply -f https://github.com/kubernetes-sigs/gateway-api/releases/download/v1.1.0/standard-install.yaml; }

cd ~/devops3/bankofantarc/k8/kustomize/istio
kubectl apply -f mygtw.yaml -n corecrud
PostScript: Probably, to be on the safe side, it might be better gtw and route are deployed after demo profile is
 deployed, as shown below.

kubectl wait --for=condition=programmed gtw common-gateway -n corecrud
I have a problem here, the gateway is not coming up, nor is it giving any error on kubectl apply command. May be the problem is resolved after the following command:
istioctl install --set profile=demo --set values.global.hub=docker.io/istio

kubectl apply -f myroute.yaml  -n corecrud

minikube tunnel

Now, I can get this beauty:
kubectl get gtw common-gateway -n corecrud
NAME             CLASS   ADDRESS         PROGRAMMED   AGE
common-gateway   istio   10.108.217.90   True         5m43s

. ./geturl.sh common-gateway corecrud
INGRESS_HOST 10.108.217.90
INGRESS_PORT 80

My next was of course to have this artificial entry in /etc/hosts:
10.108.217.90   bankofantarc.com
And then I got the following beauty which seems to suggest the Istio gateway and route configured work.
curl "http://bankofantarc.com/backsvc/clients"
[]
At some point, I will like to see whether this gateway flow works for OpenShift, obviating the need for OpenShift Route playbook fragment.
So it looks for Istio gateway to work, the istio prior install with demo profile will be needed.


A hint for future work:
https://istio.io/latest/docs/setup/platform-setup/openshift/

End Phase 3a
The following are random pastes, ignore them.
ngrok
https://ngrok.com/docs/getting-started/?os=linux
https://ngrok.com/docs/using-ngrok-with/docker/


npm install express
node webhook_server.js
http://localhost:3000/webhook

A hint: Probably for bluegreen/canary, VirtualService can be used.
