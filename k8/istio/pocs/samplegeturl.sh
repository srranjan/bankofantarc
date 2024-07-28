export INGRESS_HOST=$(kubectl get gtw httpbin-gateway -o jsonpath='{.status.addresses[0].value}')
export INGRESS_PORT=$(kubectl get gtw httpbin-gateway  -o jsonpath='{.spec.listeners[?(@.name=="http")].port}')
echo INGRESS_HOST $INGRESS_HOST
echo INGRESS_PORT $INGRESS_PORT

