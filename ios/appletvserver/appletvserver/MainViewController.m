//
//  MainViewController.m
//  appletvserver
//
//  Created by zhang wei on 13-2-12.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import "MainViewController.h"


@implementation MainViewController

@synthesize refreshButton = _refreshButton;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title = @"Main";
        self.tabBarItem = [[[UITabBarItem alloc] initWithTitle:@"Main" image:nil tag:0] autorelease];
        
        self.refreshButton=[UIButton buttonWithType:UIButtonTypeCustom];
        [self.refreshButton addTarget:self action:@selector(refresh) forControlEvents: UIControlEventTouchUpInside];
        [self.refreshButton sizeToFit];
    }
    return self;
}

-(void) refresh{
    NSArray* cookies =[[NSHTTPCookieStorage sharedHTTPCookieStorage] cookies];
    for(int i=0;i<[cookies count];i++){
        NSHTTPCookie* cookie = [cookies objectAtIndex:i];
        NSLog(@"%@=%@ %@",cookie.name,cookie.value,cookie.domain);
    }
}
- (void)loadView
{
    [super loadView];
    self.refreshButton.frame = CGRectMake(50,50,50,50);
    [self.refreshButton setTitle:@"Refresh" forState:UIControlStateNormal];
    [self.view addSubview:self.refreshButton];
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
