//
//  AppDelegate.h
//  appletvserver
//
//  Created by zhang wei on 13-2-12.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Three20/Three20.h>
#import "ASINetworkQueue.h"
#import "HTTPServer.h"

#define AppDele ((AppDelegate *)[[UIApplication sharedApplication] delegate])


@interface AppDelegate : UIResponder <UIApplicationDelegate>{
    HTTPServer *httpServer;
    ASINetworkQueue* _queue;
    NSString* _ipAddress;
}

@property (nonatomic, retain) ASINetworkQueue* queue;
@property (nonatomic, retain) NSString* ipAddress;
@end
