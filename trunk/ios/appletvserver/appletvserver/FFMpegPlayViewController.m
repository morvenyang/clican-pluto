//
//  FFMpegPlayViewController.m
//  appletvserver
//
//  Created by zhang wei on 13-2-15.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import "FFMpegPlayViewController.h"
#import "Utilities.h"
#import "Player.h"
@implementation FFMpegPlayViewController

@synthesize imageView = _imageView, video = _video, player = _player;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)loadView
{
    [super loadView];
    [self.navigationController setNavigationBarHidden:YES animated:YES];
    self.player = [[Player alloc] initWithFrame:CGRectMake(0, 0, 320, 480)];
    [self.view addSubview:self.player];
}

#define LERP(A,B,C) ((A)*(1.0-C)+(B)*C)

-(void)displayNextFrame:(NSTimer *)timer {
	NSTimeInterval startTime = [NSDate timeIntervalSinceReferenceDate];
	if (![self.video stepFrame]) {
		[timer invalidate];
		return;
	}
	self.imageView.image = self.video.currentImage;
	float frameTime = 1.0/([NSDate timeIntervalSinceReferenceDate]-startTime);
	if (lastFrameTime<0) {
		lastFrameTime = frameTime;
	} else {
		lastFrameTime = LERP(frameTime, lastFrameTime, 0.8);
	}
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

-(void)convert
{
    AVCodec *codec; 
    AVCodecContext *c= NULL; 
    int i, out_size, size, outbuf_size; 
    FILE *f; 
    AVFrame *picture; 
    uint8_t *outbuf; 
    
    printf("Video encoding\n"); 
    
    /// find the mpeg video encoder 
    codec=avcodec_find_encoder(CODEC_ID_MPEG4); 
    //codec = avcodec_find_encoder(CODEC_ID_MPEG4); 
    if (!codec) { 
        fprintf(stderr, "codec not found\n"); 
        exit(1); 
    } 
    
    c= avcodec_alloc_context(); 
    picture= avcodec_alloc_frame(); 
    
    // put sample parameters 
    c->bit_rate = 400000; 
    /// resolution must be a multiple of two 
    c->width = 320; 
    c->height = 480; 
    //frames per second 
    c->time_base= (AVRational){1,25}; 
    c->gop_size = 10; /// emit one intra frame every ten frames 
    c->max_b_frames=1; 
    c->pix_fmt =PIX_FMT_YUV420P; // PIX_FMT_YUV420P 
    
    //open it 
    if (avcodec_open(c, codec) < 0) { 
        fprintf(stderr, "could not open codec\n"); 
        exit(1); 
    } 
    
    f = fopen([@"/Users/zhangwei/Desktop/3.mp4" UTF8String], "w"); 
    if (!f) { 
        fprintf(stderr, "could not open %s\n",[@"/Users/zhangwei/Desktop/3.rmvb" UTF8String]); 
        exit(1); 
    } 
    // alloc image and output buffer 
    outbuf_size = 100000; 
    outbuf = malloc(outbuf_size); 
    size = c->width * c->height; 
    
#pragma mark - 
    AVFrame* outpic = avcodec_alloc_frame(); 
    int nbytes = avpicture_get_size(PIX_FMT_YUV420P, c->width, c->height); //this is half size of numbytes. 
    
    //create buffer for the output image 
    uint8_t* outbuffer = (uint8_t*)av_malloc(nbytes); 
    
#pragma mark -   
    for(i=1;i<48;i++) { 
        fflush(stdout); 
        
        int numBytes = avpicture_get_size(PIX_FMT_RGBA, c->width, c->height); 
        uint8_t *buffer = (uint8_t *)av_malloc(numBytes*sizeof(uint8_t)); 
        
        UIImage *image = [UIImage imageNamed:[NSString stringWithFormat:@"%d.png", i]]; 
        CGImageRef newCgImage = [image CGImage]; 
        
        CGDataProviderRef dataProvider = CGImageGetDataProvider(newCgImage); 
        CFDataRef bitmapData = CGDataProviderCopyData(dataProvider); 
        buffer = (uint8_t *)CFDataGetBytePtr(bitmapData);   
        /////////////////////////// 
        //outbuffer=(uint8_t *)CFDataGetBytePtr(bitmapData); 
        ////////////////////////// 
        avpicture_fill((AVPicture*)picture, buffer, PIX_FMT_RGBA, c->width, c->height); 
        avpicture_fill((AVPicture*)outpic, outbuffer, PIX_FMT_YUV420P, c->width, c->height);//does not have image data. 
        
        struct SwsContext* fooContext = sws_getContext(c->width, c->height, 
                                                       PIX_FMT_RGBA, 
                                                       c->width, c->height, 
                                                       PIX_FMT_YUV420P, 
                                                       SWS_FAST_BILINEAR, NULL, NULL, NULL); 
        
        //perform the conversion 
        sws_scale(fooContext, picture->data, picture->linesize, 0, c->height, outpic->data, outpic->linesize); 
        // Here is where I try to convert to YUV 
        
        // encode the image 
        
        out_size = avcodec_encode_video(c, outbuf, outbuf_size, outpic); 
        printf("encoding frame %3d (size=%5d)\n", i, out_size); 
        fwrite(outbuf, 1, out_size, f); 
        
        free(buffer); 
        buffer = NULL;       
        
    } 
    
    // get the delayed frames 
    for(; out_size; i++) { 
        fflush(stdout); 
        
        out_size = avcodec_encode_video(c, outbuf, outbuf_size, NULL); 
        printf("write frame %3d (size=%5d)\n", i, out_size); 
        fwrite(outbuf, 1, outbuf_size, f);       
    } 
    
    // add sequence end code to have a real mpeg file 
    outbuf[0] = 0x00; 
    outbuf[1] = 0x00; 
    outbuf[2] = 0x01; 
    outbuf[3] = 0xb7; 
    fwrite(outbuf, 1, 4, f); 
    fclose(f); 
    free(outbuf); 
    
    avcodec_close(c); 
    av_free(c); 
    av_free(picture); 
    av_free(outpic); 
    printf("\n"); 
}
@end
