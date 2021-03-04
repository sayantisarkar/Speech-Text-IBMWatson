package com.techcarrot.stt;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.cloud.sdk.core.http.Response;
import com.ibm.watson.developer_cloud.discovery.v1.Discovery;
import com.ibm.watson.speech_to_text.v1.SpeechToText;
import com.ibm.watson.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.speech_to_text.v1.model.SpeechRecognitionAlternative;
import com.ibm.watson.speech_to_text.v1.model.SpeechRecognitionResult;
import com.ibm.watson.speech_to_text.v1.model.SpeechRecognitionResults;
import com.techcarrot.domain.Message;

@RestController
@RequestMapping("/speech")
public class SpeechToTextController {

	@Autowired
	protected Discovery discovery;
    
    @ResponseBody
    @GetMapping("/transcribe")
    public ResponseEntity<Message> transcribeAudio() throws Exception {
    	 
    	Path path = Paths.get("C:\\Users\\sayan\\Documents\\audio-file.flac");
    	File tempFile = path.toFile();
      
        SpeechToText speechToText = new SpeechToText();
 
        //build the recognize options.
        RecognizeOptions options = new RecognizeOptions.Builder()
        .audio(new FileInputStream(tempFile))
        .contentType("audio/flac")
        .build();
        
        //execute the api service
        Response<SpeechRecognitionResults> result = speechToText.recognize(options).execute();

        
		StringBuilder transcription = new StringBuilder();
        //extract the transcript
        if( !result.getResult().getResults().isEmpty() ) {
            //load the list of transcript objects
            List<SpeechRecognitionResult> transcripts = result.getResult().getResults();
            
            //iterate over the transcripts and select the one with final bool set
            for(SpeechRecognitionResult transcript: transcripts){
                if(transcript.isXFinal()){
                	SpeechRecognitionAlternative alternative = transcript.getAlternatives().get(0);
                    transcription.append(alternative.getTranscript());
                    break;
                }
            }
        }
        Message message = new Message();
		message.setTranscript(transcription.toString());
		return new ResponseEntity<Message>(message, HttpStatus.OK);
        //System.out.printf("Transcription: %s%n", transcription.toString());
    }
}