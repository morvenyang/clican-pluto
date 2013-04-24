//
//  VideoTableItemCell.h
//  appletvserver
//
//  Created by zhang wei on 13-2-12.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//
#import "Video.h"
#import "VideoTableItem.h"
@interface VideoTableItemCell : TTTableViewCell{
    VideoTableItem* _videoTableItem;
    NSMutableArray* _imageArray;
    NSMutableArray* _titleArray;
}

@property (nonatomic, retain) VideoTableItem* videoTableItem;
@property (nonatomic, retain) NSMutableArray* titleArray;
@property (nonatomic, retain) NSMutableArray* imageArray;
@end
