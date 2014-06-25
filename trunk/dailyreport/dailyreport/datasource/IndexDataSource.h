//
//  IndexDataSource.h
//  dailyreport
//
//  Created by zhang wei on 14-5-17.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import <Three20UI/Three20UI.h>
#import "IndexListModel.h"

@interface IndexDataSource : TTListDataSource{
    IndexListModel* _indexListModel;
    BOOL _alert;
}
@property (nonatomic,retain) IndexListModel* indexListModel;
@property (nonatomic,assign) BOOL alert;
@end
