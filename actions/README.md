
https://docs.github.com/en/actions

https://docs.github.com/en/actions/use-cases-and-examples/building-and-testing/building-and-testing-java-with-maven

https://github.com/srranjan/bankofantarc/actions/runners

Clicked New runner
Clicked New self hosted runner

cd ~/Downloads
mkdir actions-runner && cd actions-runner
curl -o actions-runner-linux-x64-2.320.0.tar.gz -L https://github.com/actions/runner/releases/download/v2.320.0/actions-runner-linux-x64-2.320.0.tar.gz
tar xzf ./actions-runner-linux-x64-2.320.0.tar.gz
./config.sh --url https://github.com/srranjan/bankofantarc --token ABK4AROTOHM2TIWDARXVUVTHE7ULQ

runner group: default (just Enter).
name of runner: rajrunner
Label: rajlabel
name of work folder: Enter for default _work

It looks a svc.sh is created.

However, I don't know what is the use of above.
The actual fun happens due to the yaml file in .github/workflows folder.

For manual trigger, I might have to learn something called workflow_dispatch for yaml file.

on:
  workflow_dispatch:
 


For secrets set up:
Go to your GitHub repository.
Click on "Settings".
In the left sidebar, click on "Secrets and variables", then select "Actions".
Click the "New repository secret" button.
Enter the name (e.g., QUICKSTART_DOCKER_USERNAME and QUICKSTART_DOCKER_PASSWORD) and value (your Docker Hub username and password).



