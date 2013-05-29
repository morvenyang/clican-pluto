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
        self.title = @"迅雷";
        self.tabBarItem = [[[UITabBarItem alloc] initWithTitle:@"XunLei" image:nil tag:1] autorelease];
    }
    return self;
}

- (void)yunboAction{
    NSURLRequest* request = [NSURLRequest requestWithURL:[NSURL URLWithString:@"http://vod.xunlei.com/home.html"]];
    [self.webView  loadRequest:request];
}

- (void)lixianAction{
    NSURLRequest* request = [NSURLRequest requestWithURL:[NSURL URLWithString:@"http://lixian.xunlei.com"]];
    [self.webView  loadRequest:request];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    TTButton* yunboButton = [TTButton buttonWithStyle:@"toolbarRoundButton:" title:@"云播"];
    
    [yunboButton setFont:[UIFont systemFontOfSize:14]];

    [yunboButton addTarget:self action:@selector(yunboAction) forControlEvents:UIControlEventTouchUpInside];
    [yunboButton sizeToFit];
    
    UIBarButtonItem* yunboItem = [[UIBarButtonItem alloc] initWithCustomView:yunboButton];
    
    [self.navigationItem setLeftBarButtonItem:yunboItem animated:YES];
    
    TTButton* lixianButton = [TTButton buttonWithStyle:@"toolbarRoundButton:" title:@"离线"];
    
    [lixianButton setFont:[UIFont systemFontOfSize:14]];
    
    [lixianButton addTarget:self action:@selector(lixianAction) forControlEvents:UIControlEventTouchUpInside];
    [lixianButton sizeToFit];
    
    UIBarButtonItem* lixianItem = [[UIBarButtonItem alloc] initWithCustomView:lixianButton];
    
    [self.navigationItem setRightBarButtonItem:lixianItem animated:YES];
    
    NSURLRequest* request = [NSURLRequest requestWithURL:[NSURL URLWithString:@"http://vod.xunlei.com/home.html"]];
    self.webView = [[[UIWebView alloc] initWithFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height)] autorelease];
    [self.webView setScalesPageToFit:YES];
    [self.webView  loadRequest:request];
    [self.view addSubview:self.webView];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
