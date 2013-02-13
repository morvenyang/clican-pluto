//
//  VideoTableItemCell.m
//  appletvserver
//
//  Created by zhang wei on 13-2-12.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import "VideoTableItemCell.h"
#import "Video.h"
#import "VideoImageView.h"

@implementation VideoTableItemCell

@synthesize videoTableItem = _videoTableItem;
@synthesize titleArray = _titleArray;
@synthesize imageArray = _imageArray;

+ (CGFloat)tableView:(UITableView*)tableView rowHeightForObject:(id)object {
    return 180;
}


- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString*)identifier {
    
    self = [super initWithStyle:style reuseIdentifier:identifier];
    if (self) {
        self.titleArray = [[NSMutableArray array] retain];
        self.imageArray = [[NSMutableArray array] retain];
        for(int i=0;i<3;i++){
            VideoImageView* imageView = [[[VideoImageView alloc] autorelease] initWithFrame:CGRectZero];
            TTStyledTextLabel* titleLabel = [[[TTStyledTextLabel alloc] autorelease] init];
            [self.titleArray addObject:titleLabel];
            [self.imageArray addObject:imageView];
            
        }
        
    }
    
	return self;
}

- (void)dealloc {
    TT_RELEASE_SAFELY(_titleArray);
    TT_RELEASE_SAFELY(_imageArray);
	[super dealloc];
}
///////////////////////////////////////////////////////////////////////////////////////////////////
// UIView

- (void)layoutSubviews {
	[super layoutSubviews];
    for(int i=0;i<3;i++){
        VideoImageView* imageView = [self.imageArray objectAtIndex:i];
        TTStyledTextLabel* titleLabel = [self.titleArray objectAtIndex:i];
        imageView.frame = CGRectMake(10+103*i, 10, 93, 124);
        imageView.layer.cornerRadius = 8;
        imageView.layer.masksToBounds = YES;
        
        titleLabel.frame = CGRectMake(10+103*i, 134, 93, 36);
        [self.contentView addSubview:imageView];
        [self.contentView addSubview:titleLabel];
    }
}


///////////////////////////////////////////////////////////////////////////////////////////////////
// TTTableViewCell


- (void)setObject:(id)object {
    self.videoTableItem = object;
    for(int i=0;i<3;i++){
        VideoImageView* imageView = [self.imageArray objectAtIndex:i];
        TTStyledTextLabel* titleLabel = [self.titleArray objectAtIndex:i];
        Video* video = [self.videoTableItem.videoList objectAtIndex:i];
        imageView.urlPath = [video picUrl];
        titleLabel.text = [TTStyledText textFromXHTML:[video title] lineBreaks:YES URLs:NO];
        imageView.actionUrl=[NSString stringWithFormat:@"atvserver://qq/video/%@",video.vid];
    }
    [super setObject:object];
}


///////////////////////////////////////////////////////////////////////////////////////////////////
- (id)object {
    return self.videoTableItem;
}

@end
