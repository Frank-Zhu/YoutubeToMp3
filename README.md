# YoutubeToMp3
It's an application which uses convert Youtube videos to Mp3
<p align="center">
<img src="http://i.hizliresim.com/yL6MRn.png"/>
</p>

# Documentation

### Step 1 : Get Video ID

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
    
### Step 2 : Get Video thumbnail image from Youtube API
> I used my EasyTool library to load image.

    {
        img.L("http://img.youtube.com/vi/"+video_id+"/0.jpg");
    }
