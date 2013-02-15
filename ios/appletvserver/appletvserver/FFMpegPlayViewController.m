//
//  FFMpegPlayViewController.m
//  appletvserver
//
//  Created by zhang wei on 13-2-15.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import "FFMpegPlayViewController.h"
#import "Utilities.h"

@implementation FFMpegPlayViewController

@synthesize imageView = _imageView, video = _video;

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
    self.video = [[[VideoFrameExtractor alloc] initWithVideo:[Utilities bundlePath:@"sophie"]] autorelease];
	
    
	// set output image size
	self.video.outputWidth = 426;
	self.video.outputHeight = 320;
	
	// print some info about the video
	NSLog(@"video duration: %f",self.video.duration);
	NSLog(@"video size: %d x %d", self.video.sourceWidth, self.video.sourceHeight);
	self.imageView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"Default.png"]];
	// video images are landscape, so rotate image view 90 degrees
	[self.imageView setTransform:CGAffineTransformMakeRotation(M_PI/2)];
    [self.video seekTime:0.0];
    
	[NSTimer scheduledTimerWithTimeInterval:1.0/30
									 target:self
								   selector:@selector(displayNextFrame:)
								   userInfo:nil
									repeats:YES];
    [self.view addSubview:self.imageView];
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

@end
