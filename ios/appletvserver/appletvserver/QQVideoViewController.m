//
//  QQVideoViewController.m
//  appletvserver
//
//  Created by zhang wei on 13-2-13.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import "QQVideoViewController.h"
#import "AtvUtil.h"


@implementation QQVideoViewController

@synthesize video = _video;
@synthesize qqVideoRequestModel = _qqVideoRequestModel;
@synthesize summaryTextLabel = _summaryTextLabel;
@synthesize descriptionTextLabel = _descriptionTextLabel;
@synthesize playButton = _playButton;
@synthesize imageView = _imageView;
@synthesize reflectImageView = _reflectImageView;

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
        self.reflectImageView = [[UIView alloc] initWithFrame:CGRectMake(10, 10 , 113, 164)];
        
        self.imageView = [[[TTImageView alloc] autorelease] initWithFrame:CGRectZero];
        self.imageView.frame = CGRectMake(0, 0, 93, 124);
        self.imageView.layer.cornerRadius = 8;
        self.imageView.layer.masksToBounds = YES;
    }             
    return self;
}

- (void) videoDidFinishLoad:(Video*) video{
    self.video = video;
    
    CGRect frame = [UIScreen mainScreen].applicationFrame;
    UIScrollView* scrollView = [[UIScrollView alloc] initWithFrame:CGRectMake(0, 0, frame.size.width, frame.size.height - 92)];
    
    
    NSLog(@"picurl=%@",[video picUrl]);
    self.imageView.delegate = self;
    self.imageView.urlPath = [video picUrl];
    
    
    [scrollView addSubview:self.reflectImageView];
    
    [self.view addSubview:scrollView];
}

- (void)imageView:(TTImageView*)imageView didLoadImage:(UIImage*)image{
    if(imageView == self.imageView){
        [AtvUtil markReflect:self.reflectImageView.layer image:self.imageView.image];
    }
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
