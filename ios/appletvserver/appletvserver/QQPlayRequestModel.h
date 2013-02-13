//
//  QQPlayRequestModel.h
//  appletvserver
//
//  Created by zhang wei on 13-2-13.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//


#import "VideoItem.h"

@protocol QQPlayDelegate;

@interface QQPlayRequestModel : TTURLRequestModel{
    NSString* _videoItemId;
    id<QQPlayDelegate> _delegate;
}
@property(nonatomic,assign) id<QQPlayDelegate> delegate;
@property (nonatomic, copy)     NSString*       videoItemId;

- (id)initWithVideoItemId:(NSString*)videoItemId delegate:(id) delegate;

@end

@protocol QQPlayDelegate <NSObject>

- (void) videoItemDidFinishLoad:(VideoItem*) videoItem;

- (void) videoItemDidStartLoad:(NSString*) videoItemId;

- (void) videoItem:(NSString*)videoItemId didFailLoadWithError:(NSError*)error;

@end
