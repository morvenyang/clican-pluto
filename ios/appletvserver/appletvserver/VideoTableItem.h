//
//  VideoTableItem.h
//  appletvserver
//
//  Created by zhang wei on 13-2-12.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//
#import "Video.h"

@interface VideoTableItem : TTTableStyledTextItem{
    Video* _video;
}

@property (nonatomic, retain) Video* video;

+ (id)itemWithVideo:(Video*)video URL:(NSString*)url;
@end
