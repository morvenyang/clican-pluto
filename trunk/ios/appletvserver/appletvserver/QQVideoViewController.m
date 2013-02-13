//
//  QQVideoViewController.m
//  appletvserver
//
//  Created by zhang wei on 13-2-13.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import "QQVideoViewController.h"



@implementation QQVideoViewController

@synthesize video = _video;
@synthesize qqVideoRequestModel = _qqVideoRequestModel;

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

- (id) initWithVid:(NSString*) vid{
    if ((self = [super init])) {
        self.video = nil;
        self.qqVideoRequestModel = [[QQVideoRequestModel alloc] initWithVid:vid delegate:self];
    }             
    return self;
}

- (void) videoDidFinishLoad:(Video*) video{
    self.video = video;
    
}

- (void)videoDidStartLoad:(NSString*)vid{
    NSLog(@"load video vid:%@",vid);
}

- (void)video:(NSString*)vid didFailLoadWithError:(NSError*)error{
    NSLog(@"load video failutre vid:%@, error:%@",vid,[error description]);
}

- (void)loadView
{
    [super loadView];
    [self.qqVideoRequestModel load:TTURLRequestCachePolicyNone more:NO];
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

- (void)dealloc
{
    _qqVideoRequestModel.delegate = nil;
    TT_RELEASE_SAFELY(_video);
    TT_RELEASE_SAFELY(_qqVideoRequestModel);
    [super dealloc];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

@end
