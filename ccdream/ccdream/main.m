//
//  main.m
//  ccdream
//
//  Created by zhang wei on 12-6-3.
//  Copyright HP 2012年. All rights reserved.
//

#import <UIKit/UIKit.h>


int main(int argc, char *argv[]) {
    //iVelocity* vel = [[[iVelocity alloc] init] autorelease];
    NSAutoreleasePool * pool = [[NSAutoreleasePool alloc] init];
    int retVal = UIApplicationMain(argc, argv, nil, @"AppDelegate");
    [pool release];
    return retVal;
}
