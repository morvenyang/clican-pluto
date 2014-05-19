//
//  IndexTableItemCell.h
//  dailyreport
//
//  Created by zhang wei on 14-5-19.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import <Three20UI/Three20UI.h>
#import "IndexTableItem.h"
@interface IndexTableItemCell : TTStyledTextTableCell{
    TTImageView* _backgroundImageView;
    IndexTableItem* _indexItem;
}

@property (nonatomic, retain) IndexTableItem* indexItem;
@property (nonatomic, retain) TTImageView* backgroundImageView;

@end
