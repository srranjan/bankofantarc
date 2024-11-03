Phase 4a
Tekton pipeline webhook triggered way, somewhat different from manual way of Phase 4.
If not already done, do this
kubectl apply --filename \
https://storage.googleapis.com/tekton-releases/pipeline/latest/release.yaml
Note: The above takes some time to complete, so don't go fast on below commands.
kubectl get pods --namespace tekton-pipelines
tkn hub install task git-clone
tkn hub install task maven
tkn hub install task kaniko
kubectl apply -f mydockersecrets.yaml # The method of creating mydockersecrets.yaml, see in the README.md in kaniko* folder.
The above required irrespective of whether you want a manual or triggered thing. The same about Pipeline deploy. For some other important things, you will need to see the manual pipeline run README.md, currently in kaniko* folder, like setting docker userid password etc. A one time job 


kubectl apply --filename \
https://storage.googleapis.com/tekton-releases/triggers/latest/release.yaml

kubectl apply --filename \
https://storage.googleapis.com/tekton-releases/triggers/latest/interceptors.yaml


kubectl apply -f myrbac.yaml
kubectl create -f myPipeline.yaml
kubectl create -f myTriggerTemplate.yaml
kubectl create -f mybinding.yaml
kubectl create -f mylistener.yaml

tkn triggertemplate list
tkn triggerbinding list
tkn eventlistener  list
kubectl get svc

For some quick work, rather than using ingress gateway, use this:
kubectl port-forward -n default service/el-my-listener 8080:8080

ngrok http http://localhost:8080
Note: The above shell window will give you the first indication that webhook event has arrived.
For using gateway version, you will need to use the README.md in gateway folder, followed by this:
ngrok http http://bankofantarc.com:80
Postscript reminder: The ngrok command gives you an internet http address that has to be quickly put in the webhook configuration of the github repository (you will need to login even for public repository, as you are modifying stuff).
Note: For triggered with gateway, the above sequence might not be exactly representative.
For ngrok set up(with google authentication), you may see the lower portion in playbookNodejs
Postscript: Remember, there are 2 types of settings for github, when associated with respository, and another with profile, and in later case, you use that for going to 
Developer -> Access token etc. to generate fresh token etc.
Postscript: Working on bare metal (without VM), I can say with confidence that I was able to complete the whole tekton pipeline successfully all the way to 
push of the image to docker. This I am declaring for now for kubectl port-forward way and not for gateway way. But even without gateway way success, this is quite an 
achievement, i.e., both the yaml in triggered folder and in kaniko* folder are working end to end, no doubt. Minikube on bare metal has shown its charisma. Below is a small 
glimpse of success:
AME                         STARTED          DURATION   STATUS
clone-build-push-run-2knnz   20 minutes ago   4m5s       Succeeded


tkn pipelinerun list
Note: The above will give you the name of run instance like clone-build-push-run-q7ghl only after webhook trigger arrive, and probably the same is true of the pod shown by the below!!
kubctl get pods

Probably 2 ways to see the logs:
 tkn pipelinerun logs  clone-build-push-run-q7ghl
{
A better output than the one given below:
Pipeline still running ...
PipelineRun is still running: Tasks Completed: 0 (Failed: 0, Cancelled 0), Incomplete: 3, Skipped: 0

}
kubectl logs <podname>

Try to find which of the 2 methods above is better
A third method -triggered with gateway.

The below can be ignored, for happy paths, the above commands are sufficient. Currently, I am ultimately failing in 
git-clone. May be limitation of minikube small scale, but not sure. But most probably the above playbook is complete.
However, somehow, through the gateway route, the webhook event is not reaching listener. Some debug is shown just below:
{
Gateway debug:
1. The webhook event is reahing the ngrok m/w, but not routed to gatewway endpoint:
Session Status                online                                                                           
Account                       srranjan1234@gmail.com (Plan: Free)                                              
Version                       3.16.0                                                                           
Region                        United States (us)                                                               
Latency                       49ms                                                                             
Web Interface                 http://127.0.0.1:4040                                                            
Forwarding                    https://24c0-173-71-124-112.ngrok-free.app -> http://bankofantarc.com:80         
                                                                                                               
Connections                   ttl     opn     rt1     rt5     p50     p90                                      
                              1       0       0.00    0.00    0.05    0.05                                     
                                                                                                               
HTTP Requests                                                                                                  
-------------                                                                                                  
                                                                                                               
10:49:39.040 EDT POST /                         404 Not Found 

2. But curiously, when I try to hit gatway in browser, I see the call reaches tekton listener.
Chrome address bar: http://bankofantarc.com:80
Chrome output:
{"eventListener":"my-listener","namespace":"default","eventListenerUID":"","errorMessage":"Invalid event body format : unexpected end of JSON input"}

These 2 things look contradictary. Why ngrok will not send to bankofantarc.com defies logic, apparently.

}

{
Some debug commands:

Begin probably wrong
tkn tr list
tkn tr get my-trigger-template -o jsonpath='{.spec.resourcetemplates[*].metadata.generateName}'
tkn tr list -o jsonpath='{.items[*].spec.params[*].value}'
tkn pipelinerun logs clone-build-push-run-5hw8j -f -n default
End probably wrong

tkn triggertemplate list
NAME                  AGE
my-trigger-template   10 minutes ago
tkn triggerbinding list
NAME         AGE
my-binding   9 minutes ago

tkn eventlistener  list
NAME          AGE            URL                                                    AVAILABLE
my-listener   15 hours ago   http://el-my-listener.default.svc.cluster.local:8080   True

kubectl get svc
NAME             TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)             AGE
el-my-listener   ClusterIP   10.110.186.103   <none>        8080/TCP,9000/TCP   15h
kubernetes       ClusterIP   10.96.0.1        <none>        443/TCP             4d17

For some quick work, rather than using ingress gateway, use this:
kubectl port-forward -n default service/el-my-listener 8080:8080

ngrok http http://localhost:8080

{
Looks the push event reached the listener:
kubectl logs el-my-listener-6db44fcf89-4qkdk

.......
......
race[429953968]: [21.866801086s] [21.866801086s] END
{"severity":"info","timestamp":"2024-09-28T15:36:32.467Z","logger":"eventlistener","caller":"sink/sink.go:442","message":"ResolvedParams : [{Name:git-repo-url Value:https://github.com/srranjan/bankofantarc.git} {Name:git-revision Value:28204e06b0670a325696297a44579f0d79581762}]","commit":"616cae1-dirty","eventlistener":"my-listener","namespace":"default","/triggers-eventid":"ed919643-a9eb-4b4a-a4a6-1a1e992395b0","eventlistenerUID":"907bc828-1559-4e8d-b895-d8274441dd80","/triggers-eventid":"ed919643-a9eb-4b4a-a4a6-1a1e992395b0","/trigger":"my-trigger"}
{"severity":"info","timestamp":"2024-09-28T15:36:32.469Z","logger":"eventlistener","caller":"resources/create.go:98","message":"Generating resource: kind: &APIResource{Name:pipelineruns,Namespaced:true,Kind:PipelineRun,Verbs:[delete deletecollection get list patch create update watch],ShortNames:[pr prs],SingularName:pipelinerun,Categories:[tekton tekton-pipelines],Group:tekton.dev,Version:v1beta1,StorageVersionHash:lWZRMGzJrT4=,}, name: clone-build-push-run-","commit":"616cae1-dirty"}
{"severity":"info","timestamp":"2024-09-28T15:36:32.470Z","logger":"eventlistener","caller":"resources/create.go:106","message":"For event ID \"ed919643-a9eb-4b4a-a4a6-1a1e992395b0\" creating resource tekton.dev/v1beta1, Resource=pipelineruns","commit":"616cae1-dirty"}

}

Begin probably right

tkn triggertemplate  describe my-trigger-template
Name:        my-trigger-template
Namespace:   default

⚓ Params

 NAME                   DESCRIPTION   DEFAULT VALUE
 ∙ git-revision                       ---
 ∙ git-commit-message                 ---
 ∙ git-repo-url                       ---
 ∙ git-repo-name                      ---
 ∙ content-type                       ---
 ∙ pusher-name                        ---

📦 ResourceTemplates

 NAME    GENERATENAME            KIND          APIVERSION
 ∙ ---   clone-build-push-run-   PipelineRun   tekton.dev/v1beta1


tkn pipelinerun list
NAME                         STARTED          DURATION   STATUS
clone-build-push-run-5stbd   10 minutes ago   0s         Failed(CouldntGetPipeline)

Note: Although it fails, it shows probably that triggering from githbhub webhook event did happen.

tkn pipelinerun logs  clone-build-push-run-5stbd

Next attempt was better (after I corrected   the missing pipeline ):
 tkn pipelinerun logs  clone-build-push-run-q7ghl
{ Failure output after it ran for 12 secs:
............
............
{"level":"error","ts":1727539701.4500499,"caller":"git/git.go:53","msg":"Error running git [fetch --recurse-submodules=yes --depth=1 origin --update-head-ok --force ]: exit status 128\nfatal: unable to access 'https://github.com/srranjan/bankofantarc.git/': Could not resolve host: github.com\n","stacktrace":"github.com/tektoncd/pipeline/pkg/git.run\n\tgithub.com/tektoncd/pipeline/pkg/git/git.go:53\ngithub.com/tektoncd/pipeline/pkg/git.Fetch\n\tgithub.com/tektoncd/pipeline/pkg/git/git.go:156\nmain.main\n\tgithub.com/tektoncd/pipeline/cmd/git-init/main.go:53\nruntime.main\n\truntime/proc.go:250"}
[fetch-source : clone] {"level":"fatal","ts":1727539701.4504168,"caller":"git-init/main.go:54","msg":"Error fetching git repository: failed to fetch []: exit status 128","stacktrace":"main.main\n\tgithub.com/tektoncd/pipeline/cmd/git-init/main.go:54\nruntime.main\n\truntime/proc.go:250"}

failed to get logs for task fetch-source : container step-clone has failed  : [{"key":"StartedAt","value":"2024-09-28T16:08:15.907Z","type":3}]
Tasks Completed: 1 (Failed: 1, Cancelled 0), Skipped: 

A second way to see the logs through the logs pod way shows this:
<pre>kubectl logs clone-build-push-run-q7ghl-fetch-source-pod 
Defaulted container &quot;step-clone&quot; out of: step-clone, prepare (init), place-scripts (init)
+ &apos;[&apos; false &apos;=&apos; true ]
+ &apos;[&apos; false &apos;=&apos; true ]
+ &apos;[&apos; false &apos;=&apos; true ]
+ CHECKOUT_DIR=/workspace/output/
+ &apos;[&apos; true &apos;=&apos; true ]
+ cleandir
+ &apos;[&apos; -d /workspace/output/ ]
+ rm -rf &apos;/workspace/output//*&apos;
+ rm -rf &apos;/workspace/output//.[!.]*&apos;
+ rm -rf &apos;/workspace/output//..?*&apos;
+ test -z 
+ test -z 
+ test -z 
+ git config --global --add safe.directory /workspace/output
+ /ko-app/git-init &apos;-url=https://github.com/srranjan/bankofantarc.git&apos; &apos;-revision=&apos; &apos;-refspec=&apos; &apos;-path=/workspace/output/&apos; &apos;-sslVerify=true&apos; &apos;-submodules=true&apos; &apos;-depth=1&apos; &apos;-sparseCheckoutDirectories=&apos;
{&quot;level&quot;:&quot;error&quot;,&quot;ts&quot;:1727539701.4500499,&quot;caller&quot;:&quot;git/git.go:53&quot;,&quot;msg&quot;:&quot;Error running git [fetch --recurse-submodules=yes --depth=1 origin --update-head-ok --force ]: exit status 128\nfatal: unable to access &apos;https://github.com/srranjan/bankofantarc.git/&apos;: Could not resolve host: github.com\n&quot;,&quot;stacktrace&quot;:&quot;github.com/tektoncd/pipeline/pkg/git.run\n\tgithub.com/tektoncd/pipeline/pkg/git/git.go:53\ngithub.com/tektoncd/pipeline/pkg/git.Fetch\n\tgithub.com/tektoncd/pipeline/pkg/git/git.go:156\nmain.main\n\tgithub.com/tektoncd/pipeline/cmd/git-init/main.go:53\nruntime.main\n\truntime/proc.go:250&quot;}
{&quot;level&quot;:&quot;fatal&quot;,&quot;ts&quot;:1727539701.4504168,&quot;caller&quot;:&quot;git-init/main.go:54&quot;,&quot;msg&quot;:&quot;Error fetching git repository: failed to fetch []: exit status 128&quot;,&quot;stacktrace&quot;:&quot;main.main\n\tgithub.com/tektoncd/pipeline/cmd/git-init/main.go:54\nruntime.main\n\truntime/proc.go:250&quot;}</pre>

}

End probably right
Tekton Dashboard


To clean up, did this :
kubectl delete deploy el-my-listener
The above did not work, because things restarted. The correct is the following as this worked:
kubectl delete EventListener my-listener

Note: Prima Facie, it looks the triggering mechanism works, but the failure is due to the same reason for which the manual method also fails often. See the crux of the problem in the above log:
unable to access 'https://github.com/srranjan/bankofantarc.git/'

Also, see the keyword "git config" and CHECKOUT_DIR etc. which point to failure in task git-clone. So it looks the files are more or less perfect in this folder, at least syntactically and dependency wise.


}
