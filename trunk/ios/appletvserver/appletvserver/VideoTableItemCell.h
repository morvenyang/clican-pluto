//
//  VideoTableItemCell.h
//  appletvserver
//
//  Created by zhang wei on 13-2-12.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//
#import "Video.h"
#import "VideoTableItem.h"
@interface VideoTableItemCell : TTStyledTextTableCell{
    VideoTableItem* _videoTableItem;
    TTImageView* _videoImageView;
}

@property (nonatomic, retain) TTImageView* videoImageView;
@property (nonatomic, retain) VideoTableItem* videoTableItem;

@end
