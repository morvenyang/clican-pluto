//
//  VideoItem.h
//  appletvserver
//
//  Created by zhang wei on 13-2-13.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface VideoItem : NSObject{
    NSString* _title;
    NSString* _itemId;
    NSString* _mediaUrl;
    NSString* _vid;
}

@property (nonatomic, copy) NSString* title;
@property (nonatomic, copy) NSString* itemId;
@property (nonatomic, copy) NSString* mediaUrl;
@property (nonatomic, copy) NSString* vid;
-(void) play;
@end
