
//
//  VideoImageView.m
//  appletvserver
//
//  Created by zhang wei on 13-2-13.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import "VideoImageView.h"
#import "AppDelegate.h"
#import "XmlViewController.h"
@implementation VideoImageView

@synthesize actionUrl = _actionUrl;

-(void)dealloc{
    [_actionUrl release];    
    [super dealloc];
}

-(void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event {
    //[[AppDele jsEngine] runJS:self.actionUrl];
    XmlViewController* controler = [[XmlViewController alloc] autorelease];
    [[TTNavigator navigator].topViewController.navigationController pushViewController:controler animated:YES];
    [controler initWithScript:_actionUrl];
     [NSThread detachNewThreadSelector:@selector(runJS:) toTarget:self withObject:controler];
    
}

- (void) runJS:(id)object{
    [[AppDele jsEngine] runJS:((XmlViewController*)object).script];
}
@end
