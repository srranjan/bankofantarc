
PostScript: The files in this folder has not been verified and will not work. Rather, for tekton success, move to the kaniko based child folder to this folder (Phase 4 as explained in the corresponding README.md). Hence, can totally ignore this folder, unless you need to further attempt tekton by a second set of files. For triggered tekton version, go to the triggered folder which has also been validated to a large extent, though the flawless success has not materialized.

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

