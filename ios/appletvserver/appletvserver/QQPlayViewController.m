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
@synthesize playerViewController = _playerViewController;
@synthesize vid = _vid;

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

- (id)initWithVideoItemId:(NSString*)videoItemId vid:(NSString*)vid{
    if ((self = [super init])) {
        self.qqPlayRequestModel = [[QQPlayRequestModel alloc] initWithVideoItemId:videoItemId delegate:self];
        self.vid = vid;
    }             
    return self;
}

- (void) videoItemDidFinishLoad:(VideoItem*) videoItem{
    
    self.playerViewController = [[MPMoviePlayerViewController alloc] initWithContentURL:[NSURL fileURLWithPath:videoItem.mediaUrl]];
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(moviePlayBackDidFinish:)
                                                 name:MPMoviePlayerPlaybackDidFinishNotification object:self.playerViewController.moviePlayer];
    
    self.playerViewController.moviePlayer.controlStyle = MPMovieControlStyleFullscreen;
    //[self.view addSubview:self.playerViewController.view];
    [self.playerViewController.moviePlayer prepareToPlay];
    [self.playerViewController.moviePlayer setFullscreen:YES animated:YES];
    [self.playerViewController.moviePlayer play];
    [self presentMoviePlayerViewControllerAnimated:self.playerViewController];
}

- (void)moviePlayBackDidFinish:(NSNotification*)notification {
    int reason = [[[notification userInfo] valueForKey:MPMoviePlayerPlaybackDidFinishReasonUserInfoKey] intValue];
    if (reason == MPMovieFinishReasonUserExited) {
        //user hit the done button
        MPMoviePlayerController *moviePlayer = [notification object];
        
        [[NSNotificationCenter defaultCenter] removeObserver:self      
                                                        name:MPMoviePlayerPlaybackDidFinishNotification
                                                      object:moviePlayer];

        if ([moviePlayer respondsToSelector:@selector(setFullscreen:animated:)]) {
            [moviePlayer.view removeFromSuperview];
        }
        [self.navigationController setNavigationBarHidden:NO animated:YES];
        [self.navigationController popViewControllerAnimated:YES];
    }
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
    [self.navigationController setNavigationBarHidden:YES animated:YES];
    [self.qqPlayRequestModel load:TTURLRequestCachePolicyNone more:NO];
}

- (void)dealloc
{
    _qqPlayRequestModel.delegate = nil;
    TT_RELEASE_SAFELY(_qqPlayRequestModel);
    TT_RELEASE_SAFELY(_playerViewController);
    TT_RELEASE_SAFELY(_vid);
    [super dealloc];
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
    return YES;
}

@end
