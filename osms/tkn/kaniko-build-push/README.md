Begin Phase 4 - Tekton works with kaniko
minikube start --memory=4096 --cpus=4
minikube start --memory=16384 --cpus=8

Tekton install:
kubectl apply --filename \
https://storage.googleapis.com/tekton-releases/pipeline/latest/release.yaml
kubectl get pods --namespace tekton-pipelines

https://tekton.dev/docs/how-to-guides/kaniko-build-push/#full-code-samples

tkn hub install task git-clone
tkn hub install task maven

tkn hub install task kaniko


kubectl get tasks -A

docker login
srranjan/<commonOne> without embellishments srranjan@yahoo.com
This creats a file ~/.docker/config.json.

The output of cat ~/.docker/config.json | base64 -w0 is put in the mydockersecrets.yaml file.

cd ~/devops2/bankofantarc/k8/tkn/kaniko-build-push

kubectl apply -f mydockersecrets.yaml
secret/docker-credentials created

 kubectl apply -f myMavenTask.yaml # Not used for now
 kubectl apply -f myPipeline.yaml

 kubectl create -f myPipelineRun.yaml

pipelinerun.tekton.dev/clone-build-push-run-bdxm5 created

tkn pipelinerun logs  clone-build-push-run-bdxm5 -f 
Error: pipelines.tekton.dev "clone-build-push" not found
{
Some debug commands:
kubectl get tasks -A
kubectl get pipelines -A
kubectl get pipelineRuns -A


tkn pipeline delete --all
tkn pipelinerun delete --all
tkn task delete maven-build

secondreactv2-0.0.1-SNAPSHOT.jar

https://hub.tekton.dev/tekton/task/maven
kubectl apply -f https://api.hub.tekton.dev/v1/resource/tekton/task/maven/0.3/raw
kubectl create configmap custom-maven-settings --from-file=mymavensettings.yaml
}

{
A shorter playbook:
minikube install: 
https://minikube.sigs.k8s.io/docs/start/

curl -LO https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl

mv kubectl  /home/rranjan/.local/bin/.
chmod 755 /home/rranjan/.local/bin/kubectl

curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh
sudo usermod -aG docker $USER && newgrp docker

The above docker steps are required for minikube to work, most probably.

tkn:
https://github.com/tektoncd/cli/releases for download.
Next:
sudo dpkg -i  tektoncd-cli-0.38.0_Linux-64bit.deb

}
End Phase 4 - Tekton works with kaniko
