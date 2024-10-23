package bean;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

import jakarta.websocket.EncodeException;
import jakarta.websocket.Encoder;

public class AnnonceEncoder implements Encoder.Text<List<Annonce>> {

    private static Gson gson = new Gson();
	
	@Override
	public String encode(List<Annonce> arg0) throws EncodeException {
		// TODO Auto-generated method stub
		return gson.toJson(arg0);
	}

}
