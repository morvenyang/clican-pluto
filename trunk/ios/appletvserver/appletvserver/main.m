//
//  main.m
//  appletvserver
//
//  Created by zhang wei on 13-2-12.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "AppDelegate.h"

int main(int argc, char *argv[])
{
        NSAutoreleasePool *pool = [[NSAutoreleasePool alloc] init];
    int retVal =UIApplicationMain(argc, argv, nil, NSStringFromClass([AppDelegate class]));
        [pool release];
        return retVal;
 
}
