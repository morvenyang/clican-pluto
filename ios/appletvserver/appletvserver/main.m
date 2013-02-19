//
//  main.m
//  appletvserver
//
//  Created by zhang wei on 13-2-12.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "AppDelegate.h"
#import "ffmpeg.h"
int main(int argc, char *argv[])
{
        convert_avi_to_mp4();
        NSAutoreleasePool *pool = [[NSAutoreleasePool alloc] init];
    int retVal =UIApplicationMain(argc, argv, nil, NSStringFromClass([AppDelegate class]));
        [pool release];
        return retVal;
 
}
