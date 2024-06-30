set -e

# DockerHub 로그인
echo $DOCKERHUB_TOKEN | sudo docker login -u $DOCKERHUB_USERNAME --password-stdin

# 새로운 버전 배포 (qtudy-blue)
sudo docker pull $DOCKERHUB_USERNAME/qtudy-server:prod
sudo docker run -d -p 8002:8080 --name qtudy-server-blue $DOCKERHUB_USERNAME/qtudy-server:prod
sudo mv /etc/nginx/conf.d/qtudy-green.conf /etc/nginx/conf.d/qtudy-green.conf.backup
sudo mv /etc/nginx/conf.d/qtudy-blue.conf /etc/nginx/conf.d/qtudy-green.conf
sudo systemctl restart nginx

# 새로운 버전 확인
sleep 30
if curl -f http://127.0.0.1; then
  echo "새 버전이 정상적으로 실행되고 있습니다. 이전 컨테이너를 정리합니다."
  sudo docker stop qtudy-server-container
  sudo docker rm qtudy-server-container
  sudo docker image prune -f
else
  echo "새 버전 실행에 실패했습니다. 롤백을 진행합니다."
  sudo mv /etc/nginx/conf.d/qtudy-green.conf /etc/nginx/conf.d/qtudy-blue.conf
  sudo mv /etc/nginx/conf.d/qtudy-green.conf.backup /etc/nginx/conf.d/qtudy-green.conf
  sudo systemctl restart nginx
  sudo docker stop qtudy-server-blue
  sudo docker rm qtudy-server-blue
  sudo docker image prune -f
  exit 1
fi