//
//  XunLeiLoginViewController.m
//  appletvserver
//
//  Created by zhang wei on 13-3-12.
//
//

#import "XunLeiLoginViewController.h"


@implementation XunLeiLoginViewController

@synthesize webView = _webView;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title = @"XunLei";
        self.tabBarItem = [[[UITabBarItem alloc] initWithTitle:@"XunLei" image:nil tag:1] autorelease];
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    NSURLRequest* request = [NSURLRequest requestWithURL:[NSURL URLWithString:@"http://vod.xunlei.com/home.html"]];
    self.webView = [[[UIWebView alloc] initWithFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height)] autorelease];
    [self.webView  loadRequest:request];
    [self.view addSubview:self.webView];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
