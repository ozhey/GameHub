import { over } from 'stompjs';
import SockJS from 'sockjs-client';
import { API_ADDRESS } from '../../consts';

const getClientAndConnect = (setIsConnected, setError) => {
    let Sock = new SockJS(`${API_ADDRESS}/ws`);
    let stompClient = over(Sock);
    stompClient.connect({}, () => setIsConnected(true), (err) => setError(String(err)));
    stompClient.ws.onclose = () => {
        setIsConnected(false)
    };
    return stompClient;
}

export { getClientAndConnect };