//
//  QQVideoRequestModel.h
//  appletvserver
//
//  Created by zhang wei on 13-2-13.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//
#import "Video.h"

@protocol QQVideoDelegate;

@interface QQVideoRequestModel : TTURLRequestModel{
    NSString* _vid;
    id<QQVideoDelegate> _delegate;
}

@property(nonatomic,assign) id<QQVideoDelegate> delegate;
@property (nonatomic, copy)     NSString*       vid;

- (id)initWithVid:(NSString*)vid delegate:(id) delegate;

@end

@protocol QQVideoDelegate <NSObject>

- (void) videoDidFinishLoad:(Video*) video;

- (void)videoDidStartLoad:(NSString*)vid;

- (void)video:(NSString*)vid didFailLoadWithError:(NSError*)error;

@end
