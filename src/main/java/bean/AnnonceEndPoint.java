package bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONArray;

import jakarta.websocket.EncodeException;
import jakarta.websocket.server.*;
import jakarta.websocket.*;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint(
		value="/annonceEndPoint",
		encoders= {AnnonceEncoder.class},
		decoders= {AnnonceDecoder.class})
public class AnnonceEndPoint{
	
    private static final Map<Session,Set<Integer>> sessions = new ConcurrentHashMap<>();

	@OnOpen
    public void onOpen(Session session) throws IOException {
		
		Set<Integer> displayedAnnouncementIds = Collections.newSetFromMap(new ConcurrentHashMap<Integer, Boolean>());
		sessions.put(session, displayedAnnouncementIds);
		System.out.println("nombre de sessions "+ sessions.size());
		//sendAnnonce(session, null);
	}

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
    	if(message==null) {
    		sendAnnonce(session,null);
    		
    	}
    	else {
    		JSONArray jsonArray = new JSONArray(message);
    		int messageType=jsonArray.getInt(0);
            if(messageType==0) {
        		System.out.println("msg : "+message);
	    		sendAnnouncementsToSession(session);

            }else
            	if(messageType==1) {
            		System.out.println("msg : "+message);
		    		List<Integer> integerList = JsonUtil.parseIntegerList(jsonArray.getJSONArray(1));
		    		Set<Integer> integerSet = new LinkedHashSet<>(integerList);
		
		    		sessions.put(session, integerSet);
		    		sendAnnouncementsToSession(session);
            	}else
            		if(messageType==2) {
                		System.out.println("msg : "+message);
                		List<Integer> integerList = JsonUtil.parseIntegerList(jsonArray.getJSONArray(1));
    		    		addBid(session,integerList);
    		    		
            		}
    		
    	}
    	
    }

    @OnClose
    public void onClose(Session session) throws IOException {
    	sessions.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
    }
    
    private static void addBid(Session session,List<Integer> informations ) {
    	Metier m=new Metier();
    	boolean autoBid=false;
    	if(informations.get(4)==1) {
    		autoBid=true;
    	}
    	m.addBid(informations.get(0), informations.get(1), informations.get(2), informations.get(3),autoBid );
    }
    
    public static void updateAnnoncements(List<Integer> idListThatChanged) {
    	System.out.println("Annoncements updates");
    	for(Session s: sessions.keySet()) {
        	System.out.println("Annoncement n");
            List<Integer> annonceId = new ArrayList<>(sessions.get(s));
        	System.out.println("Set Annonce id : "+sessions.get(s));
        	System.out.println("Annonce id : "+annonceId);
            List<Integer> annonceIdNeedToChange=new ArrayList<>();
            for (int i=0;i<annonceId.size();i++) {
            	for (int j=0;j<idListThatChanged.size();j++) {
            		if(annonceId.get(i)==idListThatChanged.get(j)) {
            			annonceIdNeedToChange.add(annonceId.get(i)); 
            			System.out.println("kyn li tetbadel");
            			sendAnnonce(s, annonceId);
            		}
            	}
            }
            
    	}
    }
    
    private static void sendAnnonce(Session session,List<Integer> listAnnonceId) {
    	Metier m=new Metier();
        List<Annonce> listAnnonce=m.getAnnonce(listAnnonceId);
		try {
			session.getBasicRemote().sendObject(listAnnonce);
			Set<Integer> integerSet =null;
			if(listAnnonceId==null || listAnnonceId.size()==0) {
				integerSet = new HashSet<>();
				List<Integer> tempList=new ArrayList<>();
				for(int i=0;i<listAnnonce.size();i++) {
					tempList.add(listAnnonce.get(i).getId());
				}
				integerSet = new LinkedHashSet<>(tempList);
				System.out.println("hehehe :"+integerSet);
			}else {
				integerSet = new LinkedHashSet<>(listAnnonceId);
			}
			sessions.put(session, integerSet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EncodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
    
    public static void sendAnnouncementsToSession(Session session) {
        List<Integer> annonceId = new ArrayList<>(sessions.get(session));
        sendAnnonce(session, annonceId);
    }

	
    
}
