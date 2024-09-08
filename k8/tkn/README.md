


minikube start --memory=4096 --cpus=4

Tekton install:
kubectl apply --filename \
https://storage.googleapis.com/tekton-releases/pipeline/latest/release.yaml
kubectl get pods --namespace tekton-pipelines

docker login
This createst a file ~/.docker/config.json.

The output of cat ~/.docker/config.json | base64 -w0 is put in the mydockersecrets.yaml file.

cd ~/devops2/bankofantarc/k8/tkn/kaniko-build-push

kubectl apply -f mydockersecrets.yaml
secret/docker-credentials created


