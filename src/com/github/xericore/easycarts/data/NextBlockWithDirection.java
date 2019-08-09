package com.github.xericore.easycarts.data;

import com.github.xericore.easycarts.utilities.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Rail;
import org.bukkit.util.Vector;

public class NextBlockWithDirection
{
    private BlockFace _nextFacing;
    private Block _nextBlock;

    public NextBlockWithDirection(Block lastBlock, BlockFace previousFacing)
    {
        Vector previousDirection = Utils.getDirectionFromBlockFace(previousFacing);
        Location nextLocation = lastBlock.getLocation().clone().add(previousDirection);
        _nextBlock = nextLocation.getBlock();

        if(_nextBlock.getBlockData().getMaterial() != Material.RAIL)
        {
            _nextFacing = BlockFace.SELF;
            return;
        }

        Rail nextRail = (Rail) lastBlock.getBlockData();
        Rail.Shape nextRailShape = nextRail.getShape();


    }

    public BlockFace getNextFacing()
    {
        return _nextFacing;
    }

    public Block getNextBlock()
    {
        return _nextBlock;
    }

    public static BlockFace getNextFacingFromRailShape(Rail.Shape currentRailShape, BlockFace previousFacing)
    {
        return null;
    }
}
