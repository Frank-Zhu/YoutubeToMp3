# YoutubeToMp3
It's an application which uses convert Youtube videos to Mp3
<p align="center">
<img src="http://i.hizliresim.com/yL6MRn.png"/>
</p>

# Documentation

### Step 1 : Getting Video ID

    public static String extractYTId(String ytUrl) {
        String vId = null;
        Pattern pattern = Pattern.compile(
                "^https?://.*(?:youtu.be/|v/|u/\\w/|embed/|watch?v=)([^#&?]*).*$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(ytUrl);
        if (matcher.matches()){
            vId = matcher.group(1);
        }
        return vId;
    }
    
### Step 2 : Getting Video thumbnail image from Youtube API
> I used my [EasyTool](https://github.com/TayfunCesur/EasyTool) library to load image.

    {
        img.L("http://img.youtube.com/vi/"+video_id+"/0.jpg");
    }
    
### Step 3 : Make a Request to Youtube in MP3 API
> Just make a UrlConnection and use inputstream to get converted file.Example usage is up there.

    {
        new AsyncTask().execute("http://www.youtubeinmp3.com/fetch/?video=https://www.youtube.com/watch?v="+**VIDEO_ID**");
    }

### Step 4 : Getting Video Title
> We need the title of video to writing name of converted mp3 file.




