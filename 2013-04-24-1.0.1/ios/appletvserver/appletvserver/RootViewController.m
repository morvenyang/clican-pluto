//
//  RootViewController.m
//  appletvserver
//
//  Created by zhang wei on 13-3-12.
//
//

#import "RootViewController.h"
#import "AppDelegate.h"
#import "MainViewController.h"

@implementation RootViewController



- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        [[AppDele scriptRefreshDelegateArray] addObject:self];
    }
    return self;
}

-(void) refreshScript{
        [[AppDele jsEngine] reloadJS];
        AppDele.shareWeibo = [[AppDele jsEngine] runJS:@"appletv.isShareEnable();"];
        NSString* result = [[AppDele jsEngine] runJS:@"appletv.isSMBEnable();"];
        if(result!=nil&&[result isEqualToString:@"true"]){
            [self setTabURLs:[NSArray arrayWithObjects:
                              @"atvserver://main",
                              @"atvserver://xunlei/login",
                              @"atvserver://download",
                              @"atvserver://smb/auth",
                              @"atvserver://config",
                              nil]];
        }else{
            [self setTabURLs:[NSArray arrayWithObjects:
                              @"atvserver://main",
                              @"atvserver://xunlei/login",
                              @"atvserver://download",
                              @"atvserver://config",
                              nil]];
        }
    
}

- (void)viewDidLoad
{
    NSLog(@"RootViewController viewDidLoad");
    
    [super viewDidLoad];
    [[AppDele jsEngine] reloadJS];
    AppDele.shareWeibo = [[AppDele jsEngine] runJS:@"appletv.isShareEnable();"];
    NSString* result = [[AppDele jsEngine] runJS:@"appletv.isSMBEnable();"];
    if(result!=nil&&[result isEqualToString:@"true"]){
        [self setTabURLs:[NSArray arrayWithObjects:
                          @"atvserver://main",
                          @"atvserver://xunlei/login",
                          @"atvserver://download",
                          @"atvserver://smb/auth",
                          @"atvserver://config",
                          nil]];
    }else{
        [self setTabURLs:[NSArray arrayWithObjects:
                          @"atvserver://main",
                          @"atvserver://xunlei/login",
                          @"atvserver://download",
                          @"atvserver://config",
                          nil]];
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
