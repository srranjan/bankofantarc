
This is a continuation of Phase 4, say 4a, trying to tie tekton with argocd. This addenda was not totally successful, as I tried many ways to access argocd-server from within the minikube from Tekton pipeline run. I could have done some more experiments, like deploying my argoCd within the namespace of argocd etc., but decided not to spend time in trial and error. At least, I have all the files related to possible argocd tekton marriage, which will get solemnized sometimes in future by sheer serendipity.  The one with NN is the initial lean version, just tekton and argocd. The one with PP will be used for the final whole clone to deploy pipeline (the PP files need some more edits, while NN I hope to be almost  complete).

A small work of running argoCD with kaniko:
kubectl apply -f argoCDconfig.yaml
kubectl apply -f  argoCDsecrets.yaml
kubectl apply -f myPipelineNN.yaml
kubectl create -f myPipelineRunNN.yaml
pipelinerun.tekton.dev/argocd-deploy-run-xtjb4 created

tkn pipelinerun logs argocd-deploy-run-xtjb4 
The above first attempt failed, because I had forgotten to install the task itself, which I do below.
tkn hub install task argocd-task-sync-and-wait

Next attempt is much better, though it fails to login, but at least the script looks all good, most probably.
tkn pipeline start argocd-deploy --use-pipelinerun argocd-deploy-run-xtjb4
PipelineRun started: argocd-deploy-run-wqdjh

In order to track the PipelineRun progress run:
tkn pipelinerun logs argocd-deploy-run-wqdjh -f -n default
tkn pipelinerun logs argocd-deploy-run-wqdjh
Pipeline still running ...
task deploy-to-argocd has failed: "step-login" exited with code 20
[deploy-to-argocd : login] time="2024-10-01T13:37:34Z" level=fatal msg="dial tcp [::1]:8080: connect: connection refused"
The small problem will be tackled sometimes later - TBD.
After NN files work, we shall move to complete PP also.
The main problem is, how to hit argocd server from tekton pipeline. Should pipeline also be in argocd namespace, should the argocd application itself be in arogcd namespace, etc. etc.
