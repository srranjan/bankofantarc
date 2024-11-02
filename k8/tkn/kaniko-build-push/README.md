Begin Phase 4 - Tekton works with kaniko
minikube start --memory=4096 --cpus=4
minikube start --memory=16384 --cpus=8
For bare metal, I only have this option:
minikube start --memory=4096 --kubernetes-version=v1.31.0


(1) Tekton install:
kubectl apply --filename \
https://storage.googleapis.com/tekton-releases/pipeline/latest/release.yaml
Note: The above takes some time to complete, so don't go fast on below commands.
kubectl get pods --namespace tekton-pipelines

Tekton cli:
sudo dpkg -i  ./tektoncd-cli-0.38.1_Linux-64bit.deb

Begin Postscript
minikube start --kubernetes-version=v1.31.0
kubectl was also updated to 1.31
End Postscript

https://tekton.dev/docs/how-to-guides/kaniko-build-push/#full-code-samples
(2)
These commands can sometimes timeout, so don't panic, retry.
tkn hub install task git-clone
tkn hub install task maven

tkn hub install task kaniko


kubectl get tasks -A

docker login
srranjan/<commonOne> without embellishments srranjan@yahoo.com
This creates a file ~/.docker/config.json.

The output of cat ~/.docker/config.json | base64 -w0 is put in the mydockersecrets.yaml file.

cd ~/devops2/bankofantarc/k8/tkn/kaniko-build-push

kubectl apply -f mydockersecrets.yaml
secret/docker-credentials created

 #Don't use this: kubectl apply -f myMavenTask.yaml # Not used for now
 kubectl apply -f myPipeline.yaml
Note: The below command will not be required for triggered flow, instead you will create triggertemplate, binding and listener.
 kubectl create -f myPipelineRun.yaml
 
pipelinerun.tekton.dev/clone-build-push-run-kwgf7 created
tkn pipelinerun logs  clone-build-push-run-kwgf7
Pipeline still running ...
PipelineRun is still running: Tasks Completed: 0 (Failed: 0, Cancelled 0), Incomplete: 3, Skipped: 0
PostScript: The following should be successful, but the above is also successfully tested (may require bare metal). 
Now, for starting the pipeline, after a lot of trial and error, used this:
tkn pipeline start clone-build-push --use-pipelinerun clone-build-push-run-kwgf7
PipelineRun started: clone-build-push-run-5hw8j

In order to track the PipelineRun progress run:
tkn pipelinerun logs clone-build-push-run-5hw8j -f -n default

This is the end of main phase 4 playbook, but below are some important commands etc. There is also an important Addenda on argoCD in the argocod subfolder. 

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
