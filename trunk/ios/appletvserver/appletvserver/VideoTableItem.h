//
//  VideoTableItem.h
//  appletvserver
//
//  Created by zhang wei on 13-2-12.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//
#import "Video.h"

@interface VideoTableItem : TTTableItem{
    NSMutableArray* _videoList;
}

@property (nonatomic, retain) NSMutableArray* videoList;

+ (id)itemWithVideoList:(NSMutableArray*)videoList;
@end
