import { over } from 'stompjs';
import SockJS from 'sockjs-client';

const getClientAndConnect = (setIsConnected, setError) => {
    let Sock = new SockJS('http://localhost:8080/ws');
    let stompClient = over(Sock);
    stompClient.connect({}, () => setIsConnected(true), (err) => setError(String(err)));
    stompClient.ws.onclose = () => {
        setIsConnected(false)
    };
    return stompClient;
}

export { getClientAndConnect };