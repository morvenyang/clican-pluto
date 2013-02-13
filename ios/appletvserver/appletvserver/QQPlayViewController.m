//
//  QQPlayViewController.m
//  appletvserver
//
//  Created by zhang wei on 13-2-13.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import "QQPlayViewController.h"


@implementation QQPlayViewController

@synthesize qqPlayRequestModel = _qqPlayRequestModel;
@synthesize moviePlayer = _moviePlayer;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (id)init {
    if ((self = [self initWithNibName:nil bundle:nil])) {
        
    }
    return self;
}

- (id)initWithVideoItemId:(NSString*)videoItemId {
    if ((self = [super init])) {
        self.qqPlayRequestModel = [[QQPlayRequestModel alloc] initWithVideoItemId:videoItemId delegate:self];
    }             
    return self;
}

- (void) videoItemDidFinishLoad:(VideoItem*) videoItem{

    
    self.moviePlayer = [[MPMoviePlayerController alloc] initWithContentURL:[NSURL URLWithString:videoItem.mediaUrl]];
    [self.moviePlayer setFullscreen:YES];
    
    [[self.moviePlayer view] setFrame:CGRectMake(0, 0, 320, 240)];
    [self.moviePlayer setMovieControlMode:MPMovieControlModeDefault];
    [self.view addSubview:[self.moviePlayer view]];
    [self.moviePlayer play];

}

- (void) videoItemDidStartLoad:(NSString*) videoItemId{
    NSLog(@"load video item:%@",videoItemId);
}

- (void) videoItem:(NSString*)videoItemId didFailLoadWithError:(NSError*)error{
    NSLog(@"load video item:%@, error:%@",videoItemId,[error description]);
}

- (void)loadView
{
    [super loadView];
    [self.qqPlayRequestModel load:TTURLRequestCachePolicyNone more:NO];
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
    return UIInterfaceOrientationIsLandscape(interfaceOrientation);
}

@end
