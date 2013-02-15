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

@end
