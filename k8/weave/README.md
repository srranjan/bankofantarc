


https://gitops.weave.works/docs/open-source/getting-started/install-OSS/

minikube start --memory=4096 --cpus=4

curl -s https://fluxcd.io/install.sh | sudo bash

flux check --pre
► checking prerequisites
✔ Kubernetes 1.30.0 >=1.28.0-0
✔ prerequisites checks passed

flux bootstrap github \
  --owner=$GITHUB_USER \
  --repository=fleet-infra \
  --branch=main \
  --path=./clusters/my-cluster \
  --personal \
  --components-extra image-reflector-controller,image-automation-controller

curl --silent --location "https://github.com/weaveworks/weave-gitops/releases/download/v0.38.0/gitops-$(uname)-$(uname -m).tar.gz" | tar xz -C /tmp
sudo mv /tmp/gitops /usr/local/bin
gitops version
Current Version: 0.38.0

"Weave GitOps is an extension to Flux. "

The instructions ask to create a repos fleet-infra, but it looks it has already been created by the above command, probably the flux command.

mkdir cd ~/devops3/mygitops
mv setgit.sh cd ~/devops3/mygitops/.

cd ~/devops3/mygitops
. ./setgit.sh

cd fleet-infra
PASSWORD=P@ssword
gitops create dashboard ww-gitops \
  --password=$PASSWORD \
  --export > ./clusters/my-cluster/weave-gitops-dashboard.yaml 

Optional : ./clusters/my-cluster/weave-gitops-dashboard.yaml

git add -A && git commit -m "Add Weave GitOps Dashboard"
git push

kubectl get pods -n flux-system

NAME                                           READY   STATUS    RESTARTS   AGE
helm-controller-6f558f6c5d-tc569               1/1     Running   0          59m
image-automation-controller-8666c8ddfc-59brl   1/1     Running   0          59m
image-reflector-controller-6689cfc4bd-lhh7n    1/1     Running   0          59m
kustomize-controller-74fb56995-cxzhr           1/1     Running   0          59m
notification-controller-5d794dd575-pjl4z       1/1     Running   0          59m
source-controller-6d597849c8-v4qps             1/1     Running   0          59m

kubectl port-forward svc/ww-gitops-weave-gitops -n flux-system 9001:9001

http://localhost:9001/

admin/P@ssword


cd fleet-infra

flux create source git podinfo   --url=https://github.com/stefanprodan/podinfo   --branch=master   --interval=30s   --export > ./clusters/management/podinfo-source.yaml
To make the code above work, I had to create the management folder:
mkdir clusters/management

git add -A && git commit -m "Add podinfo source"
git push

flux create kustomization podinfo \
  --target-namespace=flux-system \
  --source=podinfo \
  --path="./kustomize" \
  --prune=true \
  --interval=5m \
  --export > ./clusters/management/podinfo-kustomization.yaml

git add -A && git commit -m "Add podinfo kustomization"
git push

Everything went without error, but podinfo did not appear ihe Applications view ??
