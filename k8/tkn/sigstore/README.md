 

https://tekton.dev/docs/getting-started/supply-chain-security/
Note: The above looks to me more promising than the ones below.
But this one also looks important:
https://tekton.dev/docs/chains/signed-provenance-tutorial/


https://tekton.dev/docs/chains/authentication/

https://tekton.dev/docs/chains/getting-started-tutorial/


https://tekton.dev/docs/pipelines/auth/  

https://tekton.dev/blog/2023/04/19/getting-to-slsa-level-2-with-tekton-and-tekton-chains/

Initial steps are the ones from README.md in kaniko* folder, repeated here.
minikube start --memory=4096 --kubernetes-version=v1.31.0 --insecure-registry "10.0.0.0/24"
Only --insecure-registry "10.0.0.0/24" added here, don't understand the full implications.

minikube addons enable registry
The above also an addition hear, not sure it is really needed.

kubectl apply --filename \
https://storage.googleapis.com/tekton-releases/pipeline/latest/release.yaml

Note: The above takes some time to complete, so don't go fast on below commands.
kubectl get pods --namespace tekton-pipelines

These commands can sometimes timeout, so don't panic, retry.
tkn hub install task git-clone
tkn hub install task maven

tkn hub install task kaniko

kubectl get tasks -A

docker login
srranjan/<commonOne> without embellishments srranjan@yahoo.com
This creates a file ~/.docker/config.json.

The output of cat ~/.docker/config.json | base64 -w0 is put in the mydockersecrets.yaml file.

Here comes a very new step for this exercise:
kubectl apply --filename \
https://storage.googleapis.com/tekton-releases/chains/latest/release.yaml

kubectl get po -n tekton-chains 
Apart from above teckton chains install, more crucial steps below:

Configure Tekton Chains to store the provenance metadata locally
kubectl patch configmap chains-config -n tekton-chains \
-p='{"data":{"artifacts.oci.storage": "", "artifacts.taskrun.format":"in-toto", "artifacts.taskrun.storage": "tekton"}}'
configmap/chains-config patched

However, Gemini suggested this:
kubectl patch configmap chains-config -n tekton-chains -p '{"data":{"artifacts.taskrun.format": "slsa/v1", "artifacts.taskrun.storage": "oci", "artifacts.oci.storage": "oci", "transparency.enabled": "true"}}'

A few peeping tom steps:
kubectl get configmap --all-namespaces | grep chains-config
kubectl get all -n tekton-chains
kubectl describe configmap chains-config  -n tekton-chains

Now, I have an arduous task of cosign installation.

While working with harness, I had to do a lot of work, let me see whether this time I can follow a shorter route.

https://docs.sigstore.dev/cosign/system_config/installation/

curl -O -L "https://github.com/sigstore/cosign/releases/latest/download/cosign-linux-amd64"
sudo mv cosign-linux-amd64 /usr/local/bin/cosign
sudo chmod +x /usr/local/bin/cosign

which cosign
/usr/local/bin/cosign
Some hope that the shorter route might have worked.
Till now, most of the commands were run in the folder  kaniko-build-push, as the YAMLs are there.
Now, coming to mainstream
cd ~/bankofantarc/k8/tkn/sigstore

cosign generate-key-pair k8s://tekton-chains/signing-secrets
Entered twice for empty password

cosign.pub is created.

Again peeping tom instinct:
kubectl describe secrets signing-secrets -n tekton-chains
Name:         signing-secrets
Namespace:    tekton-chains
Labels:       <none>
Annotations:  <none>

Type:  Opaque

Data
====
cosign.key:       653 bytes
cosign.password:  0 bytes
cosign.pub:       178 bytes

Now, delete some stuff so that some things are on a clean slate:
kubectl get pipelines -A
kubectl get pipelineRuns -A
tkn pipelinerun delete --all
tkn pipeline delete --all

Now, the next 2 commands can be run from kaniko-build-push as YAMLs are there:
kubectl apply -f myPipeline.yaml
kubectl create -f myPipelineRun.yaml
pipelinerun.tekton.dev/clone-build-push-run-5lb7j created

tkn pipelinerun logs   clone-build-push-run-5lb7j -f

export PR_UID=$(tkn pr describe --last -o  jsonpath='{.metadata.uid}')
echo $PR_UID
1cc6eb39-3472-47c5-8239-f57f83f9beb3

tkn pr describe --last \
-o jsonpath="{.metadata.annotations.chains\.tekton\.dev/signature-pipelinerun-$PR_UID}" \
| base64 -d > metadata.json

cat metadata.json | jq -r '.payload' | base64 -d | jq .

cosign verify-blob-attestation --insecure-ignore-tlog \
--key k8s://tekton-chains/signing-secrets --signature metadata.json \
--type slsaprovenance --check-claims=false /dev/null
WARNING: Skipping tlog verification is an insecure practice that lacks of transparency and auditability verification for the blob attestation.
Verified OK

Till now, according to the script of https://tekton.dev/docs/getting-started/supply-chain-security/

cosign verify --key cosign.pub index.docker.io/srranjan/reactsvccrud:latest
Error: no signatures found
main.go:69: error during command execution: no signatures found

cosign verify-attestation --key cosign.pub --type slsaprovenance index.docker.io/srranjan/reactsvccrud:latest 
Error: no matching attestations: 
main.go:74: error during command execution: no matching attestations:

This last 2 failures might need delicate debugging. But at least the steps seems to be understood.
{ 
A digression (just in case needed in future):
If ever you wish to do things like these without tekton chains, probably these 2 snippets might help:
(1) apiVersion: v1
kind: Secret
metadata:
  name: cosign-key
type: Opaque
stringData:
  cosign.key: <YOUR_COSIGN_PRIVATE_KEY>

(2) # ... (Existing pipeline definition) ...
  - name: cosign-sign
    runAfter: ["build-push"]
    taskSpec:
      steps:
      - name: sign-image
        image: cosign:latest
        volumeMounts:
        - name: cosign-key
          mountPath: /secrets
        command: ["sh", "-c"]
        args:
        - |
          cosign sign --key /secrets/cosign.key $(params.image-reference)
      volumes:
      - name: cosign-key
        secret:
          secretName: cosign-key




}



 

