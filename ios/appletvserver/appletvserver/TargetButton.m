//
//  TargetButton.m
//  appletvserver
//
//  Created by zhang wei on 13-6-4.
//
//

#import "TargetButton.h"

@implementation TargetButton

@synthesize target = _target;

+ (TargetButton*)buttonWithStyle:(NSString*)selector title:(NSString*)title target:(id) target{
    TargetButton* button = [[[self alloc] init] autorelease];
    [button setTitle:title forState:UIControlStateNormal];
    [button setStylesWithSelector:selector];
    button.target = target;
    return button;
}

-(void) dealloc{
    TT_RELEASE_SAFELY(_target);
    [super dealloc];
}

@end
