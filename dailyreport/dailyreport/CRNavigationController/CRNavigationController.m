//
//  CRNavigationController.m
//  CRNavigationControllerExample
//
//  Created by Corey Roberts on 9/24/13.
//  Copyright (c) 2013 SpacePyro Inc. All rights reserved.
//

#import "CRNavigationController.h"
#import "CRNavigationBar.h"

@interface CRNavigationController ()

@end

@implementation CRNavigationController

- (id)init {
    #ifdef __IPHONE_6_0
        self = [super initWithNavigationBarClass:[CRNavigationBar class] toolbarClass:nil];
    #else
        self = [super init];
    #endif
    if(self) {
        // Custom initialization here, if needed.
    }
    return self;
}

- (id)initWithRootViewController:(UIViewController *)rootViewController {
    #ifdef __IPHONE_6_0
        self = [super initWithNavigationBarClass:[CRNavigationBar class] toolbarClass:nil];
    #else
        self = [super init];
    #endif
    if(self) {
        self.viewControllers = @[rootViewController];
    }
    
    return self;
}

#ifdef __IPHONE_7_0
- (UIStatusBarStyle)preferredStatusBarStyle {
    return UIStatusBarStyleLightContent;
}
#endif
@end
