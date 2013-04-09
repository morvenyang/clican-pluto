//
//  RootViewController.m
//  appletvserver
//
//  Created by zhang wei on 13-3-12.
//
//

#import "RootViewController.h"
#import "AppDelegate.h"


@implementation RootViewController



- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        
    }
    return self;
}




- (void)viewDidLoad
{
    NSLog(@"RootViewController viewDidLoad");
    
    [super viewDidLoad];
    [self setTabURLs:[NSArray arrayWithObjects:
                      @"atvserver://main",
                      @"atvserver://xunlei/login",
                      @"atvserver://download",
                      @"atvserver://config",
                      nil]];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
