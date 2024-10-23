package bean;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import jakarta.websocket.DecodeException;
import jakarta.websocket.Decoder;
import jakarta.websocket.Encoder;

public class AnnonceDecoder implements Decoder.Text<List<Annonce>> {

    private static Gson gson = new Gson();
	
	@Override
	public List<Annonce> decode(String arg0) throws DecodeException {
		// TODO Auto-generated method stub
		return gson.fromJson(arg0, new TypeToken<List<Annonce>>(){}.getType());
	}

	@Override
	public boolean willDecode(String arg0) {
		// TODO Auto-generated method stub
		return (arg0 != null);
	}

	

}
